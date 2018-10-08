package service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class Service1Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Service1Controller.class);
	
	@GetMapping()
	public Flux<Service1Bean> getAll() {
		LOGGER.info("getAll");
		return Flux.just(new Service1Bean("1"), new Service1Bean("2"));
	}

	@GetMapping("/{id}")
	public Mono<Service1Bean> findById(@PathVariable("id") String id) {
		LOGGER.info("findById: id={}", id);
		return Mono.just(new Service1Bean(id));
	}
	
}