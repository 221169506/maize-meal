package za.ac.tut.maizemeal_shop.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
// import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "size")
    private String size;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "price")
    private Double price;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    // private List<OrderProduct> orderProducts;

    @Transient
    private String base64Image;

    // Constructors
    public Product() {
    }

    public Product(String name, String description, String size, Integer stockQuantity, Double price, byte[] image) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.image = image;
    }

    // Getters and setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBase64Image() {
        if (image != null) {
            base64Image = Base64.getEncoder().encodeToString(image);
        }
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    // public List<OrderProduct> getOrderProducts() {
    // return orderProducts;
    // }

    // public void setOrderProducts(List<OrderProduct> orderProducts) {
    // this.orderProducts = orderProducts;
    // }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name
                + ", description=" + description
                + ", size=" + size
                + ", stockQuantity=" + stockQuantity + ", price=" + price + ", image=" + Arrays.toString(image)
                + "]";
    }

    public byte[] getImage() {
        return image;
    }

}