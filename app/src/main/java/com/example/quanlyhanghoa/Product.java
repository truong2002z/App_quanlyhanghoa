package com.example.quanlyhanghoa;

import java.io.Serializable;
import java.util.Arrays;

public class Product  implements Serializable {
    private String productId;
    private String productName;
    private byte[] productImage;
    private String categoryName;
    private Double productPrice;
    private int productQuantity;
    private String production;

    public Product() {
    }

    public Product(String productId, String productName, byte[] productImage, String categoryname, Double productPrice, int productQuantity, String production) {
        super();
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.categoryName=categoryname;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.production = production;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductCategory() {
        return this.categoryName;
    }
    public void setProductCategory(String categoryname) {
        this.categoryName=categoryname;
    }
    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return this.productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (productQuantity != product.productQuantity) return false;
        if (productId != null ? !productId.equals(product.productId) : product.productId != null)
            return false;
        if (productName != null ? !productName.equals(product.productName) : product.productName != null)
            return false;
        if (productImage != product.productImage)
            return false;
        if (productPrice != null ? !productPrice.equals(product.productPrice) : product.productPrice != null)
            return false;
        return production != null ? production.equals(product.production) : product.production == null;

    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(productImage);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + productQuantity;
        result = 31 * result + (production != null ? production.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productImage=" + productImage +
                ", productCategory=" + categoryName +
                ", productCost=" + productPrice +
                ", productQuantity=" + productQuantity +
                ", production='" + production + '\'' +
                '}';
    }
}
