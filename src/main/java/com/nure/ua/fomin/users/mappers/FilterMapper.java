package com.nure.ua.fomin.users.mappers;

import com.nure.ua.fomin.users.api.dto.FilterDTO;
import com.nure.ua.fomin.users.entity.Filter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FilterMapper {

    List<Filter> toEntity(List<FilterDTO> filters);

    Filter toEntity(FilterDTO filter);
}
