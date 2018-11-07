package ShoppingCartService;

public class ProductServiceBean {
	private Product product;
        private double price;

	public ProductServiceBean() {
	}

	ProductServiceBean(int id, String name, double price) {
            this.product=new Product(id, name);
            this.price=price;
	}

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(double price) {
        this.price = price;
    }
       
}
class Product{
    private int productId;
    private String name;
    
    public Product(){
        
    }
    Product(int id, String name){
        this.productId=id;
        this.name=name;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }
}