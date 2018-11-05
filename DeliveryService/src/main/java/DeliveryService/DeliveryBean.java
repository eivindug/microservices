package DeliveryService;


public class DeliveryBean {

	private String id;
	private String status;
	private Service1Bean shoppingcart;

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

    public Service1Bean getShoppingCartBean() {
        return shoppingcart;
    }

    public void setShoppingcartBean(Service1Bean shoppingcart) {
        this.shoppingcart = shoppingcart;
    }

    @Override
	public String toString() {
		return "DeliveryBean [id=" + id + "], [status=" + status + "], [shoppingcart=" + shoppingcart + "]";
	}

}