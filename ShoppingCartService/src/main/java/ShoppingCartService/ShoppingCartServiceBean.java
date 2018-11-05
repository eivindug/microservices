package ShoppingCartService;

public class ShoppingCartServiceBean {
        private String email;
	private String id;

	public ShoppingCartServiceBean() {
	}

	ShoppingCartServiceBean(String id, String email) {
		this.id = id;
                this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
	@Override
	public String toString() {
		return "ShoppingCartServiceBean [id=" + id + ", email=" + email + "]";
	}

}