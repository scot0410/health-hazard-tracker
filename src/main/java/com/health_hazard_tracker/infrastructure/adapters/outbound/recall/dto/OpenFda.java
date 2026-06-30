package com.health_hazard_tracker.infrastructure.adapters.outbound.recall.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenFda(
        @JsonProperty("application_number") String[] applicationNumber,
        @JsonProperty("brand_name") String[] brandName,
        @JsonProperty("generic_name") String[] genericName,
        @JsonProperty("is_original_packager") String isOriginalPackager,
        @JsonProperty("manufacturer_name") String[] manufacturerName,
        @JsonProperty("nui") String[] nui,
        @JsonProperty("original_packager_product_ndc") String[] originalPackagerProductNdc,
        @JsonProperty("package_ndc") String[] packageNdc,
        @JsonProperty("pharm_class_cs") String[] pharmClassCs,
        @JsonProperty("pharm_class_epc") String[] pharmClassEpc,
        @JsonProperty("pharm_class_pe") String[] pharmClassPe,
        @JsonProperty("pharm_class_moa") String[] pharmClassMoa,
        @JsonProperty("product_ndc") String[] productNdc,
        @JsonProperty("product_type") String[] productType,
        @JsonProperty("route") String[] route,
        @JsonProperty("rxcui") String[] rxcui,
        @JsonProperty("spl_id") String[] splId,
        @JsonProperty("spl_set_id") String[] splSetId,
        @JsonProperty("substance_name") String[] substanceName,
        @JsonProperty("unii") String[] unii,
        @JsonProperty("upc") String[] upc) {}
