package com.azeevg.todoservice.service;

import com.azeevg.todoservice.exception.CentralisedUserServiceException;
import com.azeevg.todoservice.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CentralisedUserService {

    @Autowired
    private final RestTemplate restTemplate;

    public CentralisedUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("userInfos")
    public UserInfoDto getUser(UUID userId) {
        String apiUrl = "https://randomuser.me/api/?seed=" + userId;
        CusResponse response = restTemplate.getForObject(apiUrl, CusResponse.class, String.class);
        return Optional.ofNullable(response)
                .map(CusResponse::getResults)
                .map(list -> list.isEmpty() ? null : list.get(0))
                .orElseThrow(CentralisedUserServiceException::new);
    }

}
