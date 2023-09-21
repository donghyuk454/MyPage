package com.mong.util.log.domain.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class MetaData {
    private String className;
    private String methodName;
}
