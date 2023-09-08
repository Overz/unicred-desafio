package com.example.arquivo.services;

import com.example.arquivo.events.ArquivoEventPublisher;
import com.example.arquivo.models.Arquivo;
import com.example.arquivo.repositories.ArquivoRepository;
import com.example.common.constants.ArquivoConstants;
import com.example.common.events.ArquivoEventDTO;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import com.example.common.validations.CpfCnpj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.common.generators.Nanoid.nanoid;
import static com.example.common.utils.Sanitizer.toFixLength;

@Slf4j
@Service
public class ArquivoService {

	@Autowired
	private ArquivoRepository repo;

	@Autowired
	private ArquivoEventPublisher publisher;

	public void processarArquivoParalel(File[] files) {
		checkIfIsDone(() -> Arrays.stream(files).parallel().forEach(file -> {
			try {
				try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
					String linha;
					while ((linha = reader.readLine()) != null) {
						String cpfCnpj = linha.substring(0, 13).trim();
						String identificador = linha.substring(13, 26).trim();
						String valor = linha.substring(26).trim();

						Arquivo arquivo = Arquivo
							.builder()
							.id(toFixLength(identificador, " ", 13, identificador.length()))
							.valor(toFixLength(valor, "0", 19, 0))
							.documento(toFixLength(cpfCnpj, "0", 14, 0))
							.status(ArquivoConstants.PENDENTE)
							.dtProcessamento(System.currentTimeMillis())
							.isCpf(CpfCnpj.isCPF(cpfCnpj))
							.build();

						ArquivoEventDTO event = new ArquivoEventDTO(
							arquivo.getId(),
							arquivo.getStatus(),
							arquivo.getValor(),
							arquivo.getDocumento(),
							arquivo.getDtProcessamento(),
							arquivo.getIsCpf()
						);

						MessageStreaming messageStreaming = publisher.exchange(
							Events.ARQUIVO_ENVIA_DADOS_PARA_BOLETO_ROUTING_KEY,
							event
						);

						if (messageStreaming.getData() == null) {
							log.info("Dados inconsistentes, gravação de dados não efetivada para '{}'", arquivo);
							continue;
						}

						log.info("Salvando arquivo '{}'", arquivo);
						repo.save(arquivo);
					}
				}
			} catch (IOException e) {
				log.error("Erro processando arquivo '{}'", file.getAbsolutePath(), e);
			}
		}));
	}

	private void checkIfIsDone(Runnable runnable) {
		String id = nanoid();
		log.info("Execução async do processamento '{}' em paralelo iniciada!", id);

		CompletableFuture<Void> fn = CompletableFuture.runAsync(runnable);

		ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
		schedule.scheduleAtFixedRate(() -> {
			if (fn.isDone()) {
				log.info("Execução async do processamento '{}' em paralelo finalizado!", id);
				schedule.shutdownNow();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
}
