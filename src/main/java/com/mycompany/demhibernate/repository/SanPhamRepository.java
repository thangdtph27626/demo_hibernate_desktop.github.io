/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.demhibernate.repository;

import com.mycompany.demhibernate.entity.SanPham;
import com.mycompany.demhibernate.until.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author thang
 */
public class SanPhamRepository {
    
    public List<SanPham> getAll() {
        List<SanPham> lops = new ArrayList<>();
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            Query query = session.createQuery("FROM SanPham", SanPham.class);
            lops = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return lops;
    }
    
    public boolean add(SanPham sanPham){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            session.persist(sanPham);
            // cách 2
            // session.save(sanPham);
            // cách 3
            // session.saveOrUpdate(sanPham);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public Boolean update(SanPham sanPham) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sanPham);
            // cách 2
            // session.saveOrUpdate(sanPham);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }
    
    public boolean delete(SanPham sanPham){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            session.delete(sanPham);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public  SanPham findById(int id){
        SanPham sanPham = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            sanPham = session.get(SanPham.class, id);
//           cách 2 
//             Query query = session.createQuery("FROM SanPham WHERE id=:id", SanPham.class);
//            query.setParameter("id", id);
//            sanPham = (SanPham) query.getSingleResult();
            transaction.commit();
        } catch (Exception e) {
        }
        return sanPham;
    }
}
