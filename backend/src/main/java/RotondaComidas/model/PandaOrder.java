package RotondaComidas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import RotondaComidas.model.states.State;
import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PandaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pandaOder_generator")
    private Long orderId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<CartItem> products;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne
    private State state;

    @Column(name = "restaurant")
    private String restaurantName;
    
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "payment_id", referencedColumnName = "paymentId")
    private Payment payment;
}
