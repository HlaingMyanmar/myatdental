package org.myatdental.inoviceoptions.invoice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.inoviceoptions.invoiceadditioanal.model.InvoiceAdditionalCharge;
import org.myatdental.inoviceoptions.item.model.InvoiceItem;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.patientplanoption.model.PatientPlan;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(length = 15, nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_plan_id")
    private PatientPlan patientPlan;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate = LocalDate.now();

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal taxAmount = BigDecimal.ZERO; // Default 0.00

    @Column(name = "discount_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO; // Default 0.00

    @Column(name = "amount_paid", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountPaid = BigDecimal.ZERO; // Default 0.00


    @Column(name = "balance_due", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal balanceDue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private InvoiceStatus status = InvoiceStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvoiceAdditionalCharge> additionalCharges = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.invoiceDate == null) {
            this.invoiceDate = LocalDate.now();
        }
    }
    public enum InvoiceStatus {
        PENDING, PAID, PARTIAL, CANCELED
    }

    public void addInvoiceItem(InvoiceItem item) {
        invoiceItems.add(item);
        item.setInvoice(this);
    }

    public void removeInvoiceItem(InvoiceItem item) {
        invoiceItems.remove(item);
        item.setInvoice(null);
    }
    public void addAdditionalCharge(InvoiceAdditionalCharge charge) {
        additionalCharges.add(charge);
        charge.setInvoice(this);
    }

    public void removeAdditionalCharge(InvoiceAdditionalCharge charge) {
        additionalCharges.remove(charge);
        charge.setInvoice(null);
    }




}