package service2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class Service2Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Service2Controller.class);

	@Autowired
	private WebClient.Builder webClientBuilder;

	@GetMapping()
	public Flux<Service2Bean> getAll() {
		LOGGER.info("getAll1");
		Flux<Service1Bean> beans = webClientBuilder.build().get().uri("http://service1").retrieve().bodyToFlux(Service1Bean.class);
		Flux<Service2Bean> beans2 = beans.map(a -> new Service2Bean("Mapped:" + a.getId()));
		LOGGER.info("getAll2 (should happen right after getAll1)");
		return beans2;
	}

	@GetMapping("/{id}")
	public Mono<Service2Bean> findById(@PathVariable("id") String id) {
		LOGGER.info("findById: id={}", id);
		Mono<Service1Bean> beans = webClientBuilder.build().get().uri("http://service1/" + id).retrieve().bodyToMono(Service1Bean.class);
		Mono<Service2Bean> beans2 = beans.map(a -> new Service2Bean("Mapped:" + a.getId()));
		return beans2;
	}
	
}