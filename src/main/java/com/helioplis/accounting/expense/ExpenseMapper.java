package com.helioplis.accounting.expense;

import com.helioplis.accounting.credit.Credit;
import com.helioplis.accounting.credit.CreditUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExpenseFromDto(ExpenseUpdateDTO dto, @MappingTarget Expense entity);
}
