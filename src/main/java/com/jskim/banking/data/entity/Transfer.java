package com.jskim.banking.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "transfer"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Transfer {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal principal;
  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal fee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_account_id", referencedColumnName = "id", nullable = false, updatable = false)
  private Account fromAccount;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_account_id", referencedColumnName = "id", nullable = false, updatable = false)
  private Account toAccount;

  public Transfer(BigDecimal principal, BigDecimal fee,
      Account fromAccount, Account toAccount) {
    this.principal = principal;
    this.fee = fee;
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
  }
}
