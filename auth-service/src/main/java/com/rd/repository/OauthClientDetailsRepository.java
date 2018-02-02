package com.rd.repository;


import com.rd.domain.OAuthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OauthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {
    @Query("SELECT c FROM OAuthClientDetails c WHERE c.client_id = :client_id")
    OAuthClientDetails findByClientId(@Param("client_id") String client_id);
}
