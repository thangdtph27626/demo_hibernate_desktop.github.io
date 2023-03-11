/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.demhibernate.service;

import com.mycompany.demhibernate.entity.SanPham;
import java.util.List;

/**
 *
 * @author thang
 */
public interface SanPhamService {
    
    List<SanPham> getAll();
    
    boolean add(SanPham sanPham);
    
    boolean update(SanPham sanPham, int id);
    
    boolean delete(int id);
    
    
}
