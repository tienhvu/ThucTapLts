package com.example.demo.payload.request;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Entity.PhatTu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaoTrangRequest {
    private DaoTrang daoTrang;
    private PhatTu nguoiTruTri;
}
