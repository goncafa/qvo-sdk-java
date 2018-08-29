package cl.qvo.model;

import cl.qvo.net.http.exception.RestException;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class Customer extends Model implements Serializable {
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

    public Customer create() throws RestException {
        return (Customer) post(this);
    }

    public Customer load() throws RestException {
        return (Customer) get();
    }

    @Setter(AccessLevel.NONE) @Getter(AccessLevel.PROTECTED) private String endpoint = "customers";
}
