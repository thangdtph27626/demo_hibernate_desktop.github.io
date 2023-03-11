/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.demhibernate.service.impl;

import com.mycompany.demhibernate.entity.SanPham;
import com.mycompany.demhibernate.repository.SanPhamRepository;
import com.mycompany.demhibernate.service.SanPhamService;
import java.util.List;

/**
 *
 * @author thang
 */
public class SanPhamSerViceImpl implements SanPhamService{

    private SanPhamRepository sanPhamRepository = new SanPhamRepository();
    
    @Override
    public List<SanPham> getAll() {
        return sanPhamRepository.getAll();
    }

    @Override
    public boolean add(SanPham sanPham) {
        return  sanPhamRepository.add(sanPham);
    }

    @Override
    public boolean update(SanPham sanPham, int id) {
        SanPham updateSanPham = sanPhamRepository.findById(id);
        updateSanPham.setTen(sanPham.getTen());
        updateSanPham.setGia(sanPham.getGia());
        return sanPhamRepository.update(updateSanPham);
    }

    @Override
    public boolean delete(int id) {
       SanPham sanPham = sanPhamRepository.findById(id);
       return sanPhamRepository.delete(sanPham);
    }
    
}
