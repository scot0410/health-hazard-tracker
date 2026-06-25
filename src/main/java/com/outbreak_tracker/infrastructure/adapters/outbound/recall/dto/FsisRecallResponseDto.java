package com.outbreak_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record FsisRecallResponseDto(
        @JsonProperty("id") String id,
        @JsonProperty("recall_number") String recallNumber,
        @JsonProperty("title") String title,
        @JsonProperty("recall_date") String recallDate,
        @JsonProperty("closing_date") String closingDate,
        @JsonProperty("status") String status,
        @JsonProperty("recalled_poundage") String recalledPoundage,
        @JsonProperty("risk_level") String riskLevel,
        @JsonProperty("efficiency_classification") String efficiencyClassification,
        @JsonProperty("reason") String reason,
        @JsonProperty("dist_states") List<String> distStates,
        @JsonProperty("company_media_contact") String companyMediaContact,
        @JsonProperty("url") String url,
        @JsonProperty("last_modified") String lastModified
) {}