package com.es.core.order.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private Long id;
    private String secureId;

    private List<OrderItem> orderItems;
    /**
     *  A sum of order item prices;
     */
    private BigDecimal subtotal;
    private BigDecimal deliveryPrice;
    /**
     * <code>subtotal</code> + <code>deliveryPrice</code>
     */
    private BigDecimal totalPrice;

    @NotEmpty(message = "{order.required}")
    @Size(max = 50, message = "{order.size.50}")
    private String firstName;
    @NotEmpty(message = "{order.required}")
    @Size(max = 50, message = "{order.size.50}")
    private String lastName;
    @NotEmpty(message = "{order.required}")
    @Size(max = 50, message = "{order.size.50}")
    private String deliveryAddress;
    @NotEmpty(message = "{order.required}")
    @Pattern(regexp = "\\+375[\\-]?[0-9]{2}[\\-]?[0-9]{3}[\\-]?[0-9]{2}[\\-]?[0-9]{2}", message = "{order.phone.matcher}")
    private String contactPhoneNo;
    @Size(max = 256, message = "{order.size.256}")
    private String additionalInformation;
    private LocalDate orderingDate;

    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public LocalDate getOrderingDate() {
        return orderingDate;
    }

    public void setOrderingDate(LocalDate orderingDate) {
        this.orderingDate = orderingDate;
    }
}
