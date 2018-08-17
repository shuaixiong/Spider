package us.codecraft.webmagic.tools;

public class Store {
    String storeName;
    String priceTotal;
    String shippingOfDetails;
    String productPrice;
    String tax;
    String TotalWithoutTax;
    String productName;
    String productUPC;

    public Store(String storeName, String priceTotal, String shippingOfDetails, String productPrice, String tax) {
        this.storeName = storeName;
        this.priceTotal = priceTotal;
        this.shippingOfDetails = shippingOfDetails;
        this.productPrice = productPrice;
        this.tax = tax;

    }

    public Store() {

    }

    public String getProductUPC() {
        return productUPC;
    }

    public void setProductUPC(String productUPC) {
        this.productUPC = productUPC;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalWithoutTax() {
        return TotalWithoutTax;
    }

    public void setTotalWithoutTax(String totalWithoutTax) {
        TotalWithoutTax = totalWithoutTax;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getShippingOfDetails() {
        return shippingOfDetails;
    }

    public void setShippingOfDetails(String shippingOfDetails) {
        this.shippingOfDetails = shippingOfDetails;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
