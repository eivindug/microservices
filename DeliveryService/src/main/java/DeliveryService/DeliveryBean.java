package DeliveryService;


public class DeliveryBean {

	private String id;
	private String status;
	private ShoppingCartServiceBean shoppingcart;

	public DeliveryBean() {
	}

	DeliveryBean(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ShoppingCartServiceBean getShoppingCartBean() {
        return shoppingcart;
    }

    public void setShoppingcartBean(ShoppingCartServiceBean shoppingcart) {
        this.shoppingcart = shoppingcart;
    }

    @Override
	public String toString() {
		return "DeliveryBean [id=" + id + "], [status=" + status + "], [shoppingcart=" + shoppingcart + "]";
	}

}