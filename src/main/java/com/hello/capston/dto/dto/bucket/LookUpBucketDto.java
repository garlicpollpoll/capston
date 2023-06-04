package com.hello.capston.dto.dto.bucket;

import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.entity.enums.MemberRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LookUpBucketDto {

    List<TemporaryOrder> myBucket = new ArrayList<>();
    int bucketSize;
    int totalAmount;
    MemberRole role;

    public LookUpBucketDto(List<TemporaryOrder> myBucket, int bucketSize, int totalAmount, MemberRole role) {
        this.myBucket = myBucket;
        this.bucketSize = bucketSize;
        this.totalAmount = totalAmount;
        this.role = role;
    }
}
