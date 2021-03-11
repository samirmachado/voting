package com.business.app.integration;

import com.business.app.integration.dto.CpfValidateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cpfValidator", url = "https://user-info.herokuapp.com")
public interface CpfValidator {

    @GetMapping("/users/{cpf}")
    CpfValidateDto validateCpfToVote(@PathVariable("cpf") String cpf);
}
