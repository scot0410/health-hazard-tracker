package com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record FdaRecallDto(
    @JsonProperty("address_1") String address1,
    @JsonProperty("address_2") String address2,
    @JsonProperty("center_classification_date") String centerClassificationDate,
    @JsonProperty("city") String city,
    @JsonProperty("classification") String classification,
    @JsonProperty("code_info") String codeInfo,
    @JsonProperty("country") String country,
    @JsonProperty("distribution_pattern") String distributionPattern,
    @JsonProperty("event_id") String eventId,
    @JsonProperty("initial_firm_notification") String initialFirmNotification,
    @JsonProperty("more_code_info") String moreCodeInfo,
    @JsonProperty("openfda") OpenFda openFda,
    @JsonProperty("product_type") ProductType productType,
    @JsonProperty("product_code") String productCode,
    @JsonProperty("product_description") String productDescription,
    @JsonProperty("product_quantity") String productQuantity,
    @JsonProperty("reason_for_recall") String reasonForRecall,
    @JsonProperty("recall_initiation_date") String recallInitiationDate,
    @JsonProperty("recall_number") String recallNumber,
    @JsonProperty("recalling_firm") String recallingFirm,
    @JsonProperty("report_date") String reportDate,
    @JsonProperty("state") String state,
    @JsonProperty("status") String status,
    @JsonProperty("termination_date") String terminationDate,
    @JsonProperty("voluntary_mandated") String voluntaryMandated){}
