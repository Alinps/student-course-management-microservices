package com.example.course_service.service;

import com.example.course_service.dto.TechnologyPatchRequest;
import com.example.course_service.dto.TechnologyRequest;
import com.example.course_service.dto.TechnologyResponse;
import com.example.course_service.exception.ResourceAlreadyExistException;
import com.example.course_service.exception.ResourceNotFoundException;
import com.example.course_service.mapper.TechnologyMapper;
import com.example.course_service.models.Technology;
import com.example.course_service.repository.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyServiceImpl implements  TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);

    public TechnologyServiceImpl(
            TechnologyRepository technologyRepository,
            TechnologyMapper mapper) {
        this.technologyRepository = technologyRepository;
        this.mapper = mapper;
    }



    @Override
    public TechnologyResponse createTechnology(TechnologyRequest request) {

        log.info(
                "Creating technology. technologyCode={}, technologyName={}",
                request.getTechnologyCode(),
                request.getTechnologyName()
        );

//        if (technologyRepository.existsByTechnologyCode(request.getTechnologyCode())) {
//            throw new ResourceAlreadyExistException("Technology Code already exists");
//        }

        if (technologyRepository.existsByTechnologyName(request.getTechnologyName())) {

            log.warn(
                    "Technology creation rejected. " +
                            "Technology name already exists in course. " +
                            "technologyName={}", request.getTechnologyName()
            );
            throw new ResourceAlreadyExistException("Technology Name already exists.");
        }

        Technology technology = mapper.toEntity(request);
        Technology savedTechnology = technologyRepository.save(technology);

        return  mapper.toResponseDTO(savedTechnology);
    }



    @Override
    public TechnologyResponse getTechnologyById(Long id) {

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn( "Failed to get technology. Technology not found. technologyId={}", id);
                    return new ResourceNotFoundException("Technology not found with id: "+id);

                });


        return mapper.toResponseDTO(technology);
    }


    @Override
    public TechnologyResponse getInternalTechnologyById(Long id) {

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn( "Technology not found. technologyId={}", id);
                    return new ResourceNotFoundException("Technology not found with id: "+id);

                });


        return mapper.toResponseDTO(technology);
    }


    @Override
    public List<TechnologyResponse> getAllTechnologies() {

        List<Technology> technologies = technologyRepository.findAll();

        return mapper.toResponseDTOList(technologies);
    }

        @Override
        public TechnologyResponse updateTechnology(Long id, TechnologyRequest request   ) {

            log.info(
                    "Updating technology. technologyId={}, technologyCode={}, technologyName={}",
                    id,
                    request.getTechnologyCode(),
                    request.getTechnologyName()
            );

            Technology technology = technologyRepository.findById(id)
                    .orElseThrow(() -> {

                        log.warn("Technology update failed. Technology not found. technologyId={}", id);

                        return new ResourceNotFoundException("Technology not found with id: " + id);
                    });


            technologyRepository.findByTechnologyCode(request.getTechnologyCode())
                    .ifPresent(existingTechnology -> {

                        if (!existingTechnology.getId().equals(id)) {

                            log.warn(
                                    "Technology update rejected. Technology code already exists. " +
                                            "technologyId={}, technologyCode={}",
                                    id,
                                    request.getTechnologyCode()
                            );

                            throw new ResourceAlreadyExistException("Technology code already exists.");
                        }
                    });


            technologyRepository.findByTechnologyName(request.getTechnologyName())
                    .ifPresent(existingTechnology -> {

                        if (!existingTechnology.getId().equals(id)) {

                            log.warn(
                                    "Technology update rejected. Technology name already exists. " +
                                            "technologyId={}, technologyName={}",
                                    id,
                                    request.getTechnologyName()
                            );

                            throw new ResourceAlreadyExistException("Technology name already exists.");
                        }
                    });

            mapper.updateEntity(request,technology);

            Technology updatedTechnology = technologyRepository.save(technology);

            log.info(
                    "Technology updated successfully. technologyId={}, technologyCode={}, technologyName={}",
                    updatedTechnology.getId(),
                    updatedTechnology.getTechnologyCode(),
                    updatedTechnology.getTechnologyName()
            );

            return mapper.toResponseDTO(updatedTechnology);
        }

    @Override
    public TechnologyResponse patchTechnology(Long id, TechnologyPatchRequest request) {

        log.info(
                "Patching technology. technologyId={}, technologyCode={}, technologyName={}",
                id,
                request.getTechnologyCode(),
                request.getTechnologyName()
        );

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Technology patch failed. Technology not found. technologyId={}", id);

                    return new ResourceNotFoundException("Technology not found with id: " + id);

                });

        technologyRepository.findByTechnologyName(request.getTechnologyName())
                .ifPresent(existingTechnology -> {
                    if (!existingTechnology.getId().equals(id)) {
                        log.warn(
                                "Technology patch rejected. Technology name already exists. " +
                                        "technologyId={}, technologyCode={}",
                                id,
                                request.getTechnologyCode()
                        );
                        throw new ResourceAlreadyExistException("Technology already exists.");
                    }
                });

        technologyRepository.findByTechnologyCode(request.getTechnologyCode())
                .ifPresent(existingTechnology -> {
                    if (!existingTechnology.getId().equals(id)) {
                        log.warn(
                                "Technology patch rejected. Technology code already exists. " +
                                        "technologyId={}, technologyCode={}",
                                id,
                                request.getTechnologyCode()
                        );
                        throw new ResourceAlreadyExistException("Technology already exists.");
                    }
                });

        mapper.patchEntity(request, technology);

        Technology updatedTechnology = technologyRepository.save(technology);

        log.info(
                "Technology patched successfully. technologyId={}, technologyCode={}, technologyName={}",
                updatedTechnology.getId(),
                updatedTechnology.getTechnologyCode(),
                updatedTechnology.getTechnologyName()
        );

        return  mapper.toResponseDTO(updatedTechnology);

    }

    public void deleteTechnology(Long id) {

        log.info("Deleting technology . technologyId={}", id);

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Technology delete failed. Technology not found. technologyId={}", id);
                    return new ResourceNotFoundException("Technology not found with id: " + id);

                });
        technologyRepository.delete(technology);
    }





}
