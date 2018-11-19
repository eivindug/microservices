package ShoppingCartService;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ShoppingCartServiceController implements ApplicationListener<ApplicationReadyEvent> {
        
        ArrayList<ShoppingCartServiceBean> shoppingCarts= new ArrayList<ShoppingCartServiceBean>();
        ArrayList<Product> products= new ArrayList<Product>();
        
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceController.class);

	@Autowired
	private WebClient.Builder webClientBuilder;
        private WebClient.Builder bodyBuilder;
       // private static WebClient.Builder webClientBuilder2;


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Service1Controller: init");
        
        Flux<Product> messages = webClientBuilder.build().get().uri("http://product-service/products/get").retrieve().bodyToFlux(Product.class);
        messages.subscribe(message -> products.add(message));
    }
	@PostMapping(value="/post", consumes = MediaType.APPLICATION_JSON_VALUE)
	Mono<Void> create(@RequestBody Item item) {
                Mono<Double>price=webClientBuilder.build().get().uri("http://product-service/products/getprice/" + item.getId()).retrieve().bodyToMono(Double.class);
                String name="";
                for(int i=0; i<products.size(); i++){
                    if(products.get(i).getProductId()==item.getId()) name=products.get(i).getName();
                }
                ProductServiceBean p= new ProductServiceBean(item.getId(), name, 0.0);
                price.subscribe(value -> p.setPrice(value));
                
                int index=-1;
                for(int x=0; x<shoppingCarts.size(); x++){
                    if(shoppingCarts.get(x).getEmail().equals(item.getEmail())) index=x;
                }
                if(index==-1){
                    shoppingCarts.add(new ShoppingCartServiceBean((shoppingCarts.size()+1), item.getEmail()));
                    shoppingCarts.get(shoppingCarts.size()-1).addToShoppingCart(p);
                }else{
                    shoppingCarts.get(index).addToShoppingCart(p);
                }
		return Mono.empty();
	}
        @GetMapping(value="/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public Flux<ProductServiceBean> getPrice(@PathVariable String email){
                for(int x=0; x<shoppingCarts.size(); x++){
                    if(shoppingCarts.get(x).getEmail().equals(email)){
                        Flux<ProductServiceBean> sequence=Flux.fromIterable(shoppingCarts.get(x).getShoppingCart());
                        return sequence;
                    }
                }
		return null;
        }
        @PostMapping(value="/{email}")
        public Mono<String> postPurchase(@PathVariable String email){
            int index=-1;
            for(int x=0; x<shoppingCarts.size(); x++){
                    if(shoppingCarts.get(x).getEmail().equals(email)){
                        index=x;
                        LOGGER.info("Found email" + email);
                        break;
                    }
            }
            WebClient.RequestHeadersSpec p = webClientBuilder.filter(logRequest()).build().post().uri("http://delivery-service/createOrder").body(BodyInserters.fromObject(shoppingCarts.get(index)));
            Mono<String> id= p.retrieve().bodyToMono(String.class);
            Mono<String> returnMono=Mono.create(emitter -> id.subscribe(value -> {
                deliver(value);
            emitter.success(value);}) );
            
            
            //id.subscribe(value -> deliver(value));
            return returnMono;
        }
        private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
        private Mono<Void> deliver(String id){
            WebClient.RequestHeadersSpec p = webClientBuilder.filter(logRequest()).build().post().uri("http://delivery-service/deliverOrder/{orderId}", id);
            return p.retrieve().bodyToMono(Void.class);
        }

    private static class Item {
        private int id;
        private String email;
        
        public Item() {
        }
        public Item(int id, String email){
            this.id=id;
            this.email=email;
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }

}