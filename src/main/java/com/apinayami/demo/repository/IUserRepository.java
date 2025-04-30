package com.apinayami.demo.repository;

import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserModel,Long> {
    List<UserModel> findByType(Role role);
    UserModel findById(long id);
    UserModel findByIdAndType(long id, Role role);
    UserModel findByEmail(String email);
    List<UserModel> getAllByType(Role role);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    UserModel getUserByEmail(String email);
    boolean existsByEmailAndIdNot(String email, long id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, long id);
    @Query("SELECT COUNT(u) > 0 FROM UserModel u " +
            "JOIN u.listBill b " +
            "JOIN b.items li " +
            "WHERE u.email = :email AND li.productModel.id = :proId AND b.status = 'COMPLETED'")
    boolean checkUserPurchaseProduct(String email, long proId);
    UserModel findUserModelByEmailAndActive(String emai,boolean active);
    @Transactional
    @Modifying
    @Query("UPDATE UserModel u " +
            "SET u.email = :email, u.password = :password " +
            "WHERE u.type = 'ADMIN'")
    int updateInformationOfAdmin(@Param("email") String email, @Param("password") String password);
}
