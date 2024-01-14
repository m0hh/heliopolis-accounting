package com.helioplis.accounting.pay;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PayService {
    private final UserRepository userRepository;
    private final ShiftRepo shiftRepo;
    private final PayRepo payRepo;
    @Transactional
    public void createPays(){
        LocalDateTime afterDate = LocalDateTime.now();
        LocalDateTime beforeDate = afterDate.minusMonths(1);
        List<UserHelioplis> users = userRepository.findAll();
        for (UserHelioplis user: users) {
            double totalHours = 0.0;
            double totalPay;
            double totalDeduction;
            Pay pay = new Pay();

            List<Shift> shifts = shiftRepo.findFilter(beforeDate, afterDate, user.getId());
            for (Shift shift : shifts) {
                pay.addShift(shift);
                totalHours += shift.getCreatedAt().until(shift.getClosed_at(), ChronoUnit.HOURS);
            }
            totalPay = totalHours * user.getHourlyRate().doubleValue();
            totalDeduction = (168.0 * user.getHourlyRate().doubleValue()) - totalPay;
            pay.setCreatedAt(LocalDate.now());
            pay.setTotalPay(BigDecimal.valueOf(totalPay));
            pay.setTotalHours(BigDecimal.valueOf(totalHours));
            pay.setTotalDeduction(BigDecimal.valueOf(totalDeduction));
            pay.setUser(user);
            payRepo.save(pay);
            // send notification
        }

    }
}
