package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record FsisRecallDto(
        @JsonProperty("field_title") String title,
        @JsonProperty("field_recall_number") String recallNumber,
        @JsonProperty("field_recall_number_export") String recallNumberExport,
        @JsonProperty("field_recall_url") String recallUrl,
        @JsonProperty("field_active_notice") String activeNotice, // Populates "True"/"False" strings
        @JsonProperty("field_states") List<String> states,
        @JsonProperty("field_archive_recall") String archiveRecall,
        @JsonProperty("field_closed_year") String closedYear,
        @JsonProperty("field_company_media_contact") List<String> companyMediaContact,
        @JsonProperty("field_distro_list") List<String> distroList,
        @JsonProperty("field_en_press_release") List<String> enPressRelease,
        @JsonProperty("field_establishment") List<String> establishment,
        @JsonProperty("field_labels") List<String> labels,
        @JsonProperty("field_media_contact") String mediaContact,
        @JsonProperty("field_risk_level") String riskLevel,
        @JsonProperty("field_last_modified_date") String lastModifiedDate,
        @JsonProperty("field_press_release") List<String> pressRelease,
        @JsonProperty("field_processing") List<String> processing,
        @JsonProperty("field_product_items") List<String> productItems,
        @JsonProperty("field_qty_recovered") String qtyRecovered,
        @JsonProperty("field_recall_classification") String recallClassification,
        @JsonProperty("field_recall_date") String recallDate,
        @JsonProperty("field_recall_reason") List<String> recallReason,
        @JsonProperty("field_recall_type") String recallType,
        @JsonProperty("field_related_to_outbreak") String relatedToOutbreak,
        @JsonProperty("field_summary") String summary,
        @JsonProperty("field_year") String year,
        @JsonProperty("langcode") String langcode,
        @JsonProperty("field_has_spanish") String hasSpanish
) {}

