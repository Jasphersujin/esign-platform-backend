package com.esign.platform.organizationmanagement.organization.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationContactRequest;
import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationCreateRequest;
import com.esign.platform.organizationmanagement.organization.dto.req.OrganizationUpdateRequest;
import com.esign.platform.organizationmanagement.organization.dto.res.OrganizationContactResponse;
import com.esign.platform.organizationmanagement.organization.dto.res.OrganizationResponse;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationContactEntity;
import com.esign.platform.organizationmanagement.organization.entity.OrganizationEntity;
import com.esign.platform.organizationmanagement.organization.repository.OrganizationContactRepository;
import com.esign.platform.organizationmanagement.organization.repository.OrganizationRepository;
import com.esign.platform.organizationmanagement.organization.service.OrganizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationContactRepository contactRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    @Transactional
    public OrganizationResponse create(
            OrganizationCreateRequest request) {

        // -----------------------------------------------------
        // 1. Create Organization
        // -----------------------------------------------------

        OrganizationEntity organization =
                new OrganizationEntity();

        organization.setOrgName(request.getOrgName());
        organization.setOrgLogo(request.getOrgLogo());
        organization.setCountry(request.getCountry());
        organization.setState(request.getState());
        organization.setCity(request.getCity());
        organization.setAddressLine1(request.getAddressLine1());
        organization.setAddressLine2(request.getAddressLine2());
        organization.setPostalCode(request.getPostalCode());
        organization.setWebsite(request.getWebsite());
        organization.setBusinessType(request.getBusinessType());

        organization.setActive(true);
        organization.setDeleted(false);

        organization = organizationRepository.save(organization);


        // -----------------------------------------------------
        // 2. Create Contact
        // -----------------------------------------------------

        if (request.getContact() != null) {

            OrganizationContactEntity contact =
                    createContact(
                            organization,
                            request.getContact()
                    );

            contactRepository.save(contact);
        }


        // -----------------------------------------------------
        // 3. Return complete response
        // -----------------------------------------------------

        return mapToResponse(organization);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getById(UUID id) {

        OrganizationEntity organization =
                organizationRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found: " + id
                                )
                        );

        return mapToResponse(organization);
    }


    // =========================================================
    // LIST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAll(
            String search,
            Pageable pageable) {

        Page<OrganizationEntity> organizations;

        if (search != null && !search.trim().isEmpty()) {

            organizations =
                    organizationRepository
                            .findByDeletedFalseAndOrgNameContainingIgnoreCase(
                                    search.trim(),
                                    pageable
                            );

        } else {

            organizations =
                    organizationRepository
                            .findByDeletedFalse(pageable);
        }

        return organizations.map(this::mapToResponse);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    @Transactional
    public OrganizationResponse update(
            UUID id,
            OrganizationUpdateRequest request) {

        OrganizationEntity organization =
                organizationRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found: " + id
                                )
                        );


        // -----------------------------------------------------
        // 1. Update Organization
        // -----------------------------------------------------

        organization.setOrgName(request.getOrgName());
        organization.setOrgLogo(request.getOrgLogo());
        organization.setCountry(request.getCountry());
        organization.setState(request.getState());
        organization.setCity(request.getCity());
        organization.setAddressLine1(request.getAddressLine1());
        organization.setAddressLine2(request.getAddressLine2());
        organization.setPostalCode(request.getPostalCode());
        organization.setWebsite(request.getWebsite());
        organization.setBusinessType(request.getBusinessType());


        organizationRepository.save(organization);


        // -----------------------------------------------------
        // 2. Update Contact
        // -----------------------------------------------------

        if (request.getContact() != null) {

            List<OrganizationContactEntity> contacts =
                    contactRepository
                            .findByOrganizationIdAndDeletedFalse(id);

            OrganizationContactEntity contact;

            if (contacts.isEmpty()) {

                contact = createContact(
                        organization,
                        request.getContact()
                );

            } else {

                // For now update primary/current contact
                contact = contacts.stream()
                        .filter(c -> Boolean.TRUE.equals(c.getPrimary()))
                        .findFirst()
                        .orElse(contacts.get(0));

                updateContact(
                        contact,
                        request.getContact()
                );
            }

            contactRepository.save(contact);
        }


        return mapToResponse(organization);
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    @Transactional
    public void softDelete(UUID id) {

        OrganizationEntity organization =
                organizationRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found: " + id
                                )
                        );


        // -----------------------------------------------------
        // Delete Organization
        // -----------------------------------------------------

        organization.setDeleted(true);
        organization.setActive(false);

        organizationRepository.save(organization);


        // -----------------------------------------------------
        // Delete Contacts
        // -----------------------------------------------------

        List<OrganizationContactEntity> contacts =
                contactRepository
                        .findByOrganizationIdAndDeletedFalse(id);

        for (OrganizationContactEntity contact : contacts) {

            contact.setDeleted(true);
            contact.setActive(false);

            contactRepository.save(contact);
        }
    }


    // =========================================================
    // CREATE CONTACT
    // =========================================================

    private OrganizationContactEntity createContact(
            OrganizationEntity organization,
            OrganizationContactRequest request) {

        OrganizationContactEntity contact =
                new OrganizationContactEntity();

        contact.setOrganization(organization);

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setDesignation(request.getDesignation());
        contact.setEmail(request.getEmail());
        contact.setCountryCode(request.getCountryCode());
        contact.setPhoneNumber(request.getPhoneNumber());

        contact.setPrimary(true);
        contact.setActive(true);
        contact.setDeleted(false);

        contact.setEffectiveFrom(LocalDate.now());

        return contact;
    }


    // =========================================================
    // UPDATE CONTACT
    // =========================================================

    private void updateContact(
            OrganizationContactEntity contact,
            OrganizationContactRequest request) {

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setDesignation(request.getDesignation());
        contact.setEmail(request.getEmail());
        contact.setCountryCode(request.getCountryCode());
        contact.setPhoneNumber(request.getPhoneNumber());
    }


    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    private OrganizationResponse mapToResponse(
            OrganizationEntity organization) {

        List<OrganizationContactResponse> contacts =
                contactRepository
                        .findByOrganizationIdAndDeletedFalse(
                                organization.getId()
                        )
                        .stream()
                        .map(this::mapContact)
                        .toList();

        return OrganizationResponse.builder()
                .id(organization.getId())
                .orgName(organization.getOrgName())
                .orgLogo(organization.getOrgLogo())
                .country(organization.getCountry())
                .state(organization.getState())
                .city(organization.getCity())
                .addressLine1(organization.getAddressLine1())
                .addressLine2(organization.getAddressLine2())
                .postalCode(organization.getPostalCode())
                .website(organization.getWebsite())
                .businessType(organization.getBusinessType())
                .active(organization.getActive())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .contacts(contacts)
                .build();
    }


    private OrganizationContactResponse mapContact(
            OrganizationContactEntity contact) {

        return OrganizationContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .designation(contact.getDesignation())
                .email(contact.getEmail())
                .countryCode(contact.getCountryCode())
                .phoneNumber(contact.getPhoneNumber())
                .primary(contact.getPrimary())
                .active(contact.getActive())
                .effectiveFrom(contact.getEffectiveFrom())
                .effectiveTo(contact.getEffectiveTo())
                .build();
    }
}