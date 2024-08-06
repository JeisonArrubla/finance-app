package com.jeison.finance.finance_app.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    @JsonIgnoreProperties({ "balance", "user", "outgoingTransactions", "incomingTransactions" })
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    @JsonIgnoreProperties({ "balance", "user", "outgoingTransactions", "incomingTransactions" })
    private Account destinationAccount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Debes indicar el tipo de transacción")
    private TransactionType type;

    @NotNull(message = "Debes indicar el monto")
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime date;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String description;

    public Transaction() {
    }

    public Transaction(Long id, Account sourceAccount, Account destinationAccount, TransactionType type,
            BigDecimal amount, LocalDateTime date, String description) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
