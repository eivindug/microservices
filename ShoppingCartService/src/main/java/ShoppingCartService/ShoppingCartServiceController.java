package ShoppingCartService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ShoppingCartServiceController implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceController.class);

	@Autowired
	private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Service1Controller: init");

        // Subscribe to stream
        Flux<String> messages = webClientBuilder.build().get().uri("http://service-2/messages").retrieve().bodyToFlux(String.class);
        messages.subscribe(message -> LOGGER.info("subscriber: " + message));
    }

	@GetMapping()
	public Flux<ShoppingCartServiceBean> getAll() {
		LOGGER.info("getAll");
		waitFor2Seconds();

		LOGGER.info("returning flux");
		return Flux.just(new ShoppingCartServiceBean("1", "kakemann@kake.no"), new ShoppingCartServiceBean("2", "pepperkake@kake.no"));
	}

	@PostMapping()
	Mono<Void> create(@RequestBody ShoppingCartServiceBean bean) {
		LOGGER.info("create: id={}", bean.getId());
		waitFor2Seconds();
		return Mono.empty();
	}

	private void waitFor2Seconds() {
		LOGGER.info("sleeping for 2 seconds");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}