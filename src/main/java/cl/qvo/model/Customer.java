package cl.qvo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Customer implements Serializable {
    /**
     * Object unique identification
     */
    private String id;

    /**
     * Customer default payment method
     */
    private Card defaultPaymentMethod;

    /**
     * Customer name
     */
    private String name;

    /**
     * Customer email
     */
    private String email;

    /**
     * Customer active subscriptions
     */
    private List<Subscription> subscriptions;

    /**
     * Customer active cards
     */
    private List<Card> cards;

    /**
     * Customer linked transactions
     */
    private List<Transaction> transactions;

    /**
     * Object create date
     */
    private Date createdAt;

    /**
     * Object update date
     */
    private Date updatedAt;
}
