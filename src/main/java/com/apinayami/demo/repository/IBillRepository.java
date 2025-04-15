package com.apinayami.demo.repository;

import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface IBillRepository extends JpaRepository<BillModel, Long> {
    List<BillModel> findByCustomerModelOrderByCreatedAtDesc(UserModel user);

    BillModel findByIdAndCustomerModel(Long billId, UserModel user);


    @Query("SELECT COUNT(b) FROM BillModel b WHERE b.status = :status")
    Long countBillsByStatus(@Param("status") EBillStatus status);

    @Query("SELECT SUM(b.totalPrice) FROM BillModel b WHERE b.status = :status")
    Double totalRevenue(@Param("status") EBillStatus status);

    @Transactional
    @Query("SELECT SUM(od.quantity * (od.unitPrice-p.originalPrice)) as Q FROM LineItemModel od JOIN od.productModel p JOIN od.billModel o WHERE  o.status = :status")
    Double totalProfit(@Param("status") EBillStatus status);

    @Transactional
    @Query("from BillModel od where od.createdAt >= ?1 and od.createdAt <= ?2 and od.status = :status order by od.createdAt ")
    List<BillModel> revenueByTime(LocalDateTime startDate, LocalDateTime endDate, @Param("status") EBillStatus status);

    @Query("SELECT p, SUM(od.quantity) as totalQuantity, COUNT(DISTINCT o.id) as totalOrders " +
            "FROM LineItemModel od " +
            "JOIN od.productModel p " +
            "JOIN od.billModel o " +
            "WHERE o.createdAt >= ?1 and o.createdAt <= ?2 and o.status = :status " +
            "GROUP BY p.productName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> topSeller(LocalDateTime startDate, LocalDateTime endDate, @Param("status") EBillStatus status);

    List<BillModel> findByPaymentModel_PaymentStatusAndCreatedAtBefore(EPaymentStatus pending, LocalDateTime cutoffTime);

    List<BillModel> findByStatusAndUpdatedAtBefore(EBillStatus shipped, LocalDateTime cutoffTime);

    List<BillModel> findAllByOrderByCreatedAtDesc();
    
}