package DeliveryService;


import java.util.ArrayList;

public class ShoppingCartServiceBean {
        private String email;
	private int id;
        private ArrayList<ProductServiceBean> shoppingCart= new ArrayList<ProductServiceBean>();
        
	public ShoppingCartServiceBean() {
	}

	ShoppingCartServiceBean(int id, String email) {
		this.id = id;
                this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public void addToShoppingCart(ProductServiceBean bean){
            shoppingCart.add(bean);
        }
        public ArrayList<ProductServiceBean> getShoppingCart() {
            return shoppingCart;
        }
        
        
	@Override
	public String toString() {
		return "ShoppingCartServiceBean [id=" + id + ", email=" + email + "]";
	}

}