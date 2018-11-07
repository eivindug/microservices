package ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductServiceController implements ApplicationListener<ApplicationReadyEvent> {
        private ArrayList <ProductServiceBean> items = new ArrayList<ProductServiceBean>(Arrays.asList(new ProductServiceBean(1, "Milk", 30)
                ,new ProductServiceBean(2, "Bread", 25.50), new ProductServiceBean(3, "Egg", 39.99), new ProductServiceBean(4, "Cheese", 49)
                ,new ProductServiceBean(5, "Soda", 15)));
               
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceController.class);

	@Autowired
	private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Service1Controller: init");

        //Flux<String> messages = webClientBuilder.build().get().uri("http://service-2/messages").retrieve().bodyToFlux(String.class);
        //messages.subscribe(message -> LOGGER.info("subscriber: " + message));
    }

	@GetMapping(value="/products/get", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Product> getAll() {
		LOGGER.info("getAll");

		LOGGER.info("returning flux");
                ArrayList<Product> list = new ArrayList<Product>();
                for(int i=0; i<items.size(); i++){
                    list.add(items.get(i).getProduct());
                }
                Flux<Product> sequence=Flux.fromIterable(list) ;
		return sequence;
	}

	@GetMapping(value="/products/getprice/{productId}", produces = MediaType.APPLICATION_JSON_VALUE) 
        public Mono<Double> getPrice(@PathVariable int productId){
            double price=0;
            for(int i=0; i<items.size(); i++){
                if(items.get(i).getProduct().getProductId()==productId) price=items.get(i).getPrice();                
            }
            return Mono.just(price);
        }   
}