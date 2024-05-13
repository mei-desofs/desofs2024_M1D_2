package backend.service.impl;

import backend.repository.PhotoRepository;
import backend.service.PhotoService;
import backend.service.dto.PhotoDTO;
import backend.service.mapper.PhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    @Override
    public Mono<PhotoDTO> save(PhotoDTO photoDTO) {
        log.debug("Request to save Photo : {}", photoDTO);
        return photoRepository.save(photoMapper.toEntity(photoDTO)).map(photoMapper::toDto);
    }

    @Override
    public Mono<PhotoDTO> update(PhotoDTO photoDTO) {
        log.debug("Request to update Photo : {}", photoDTO);
        return photoRepository.save(photoMapper.toEntity(photoDTO)).map(photoMapper::toDto);
    }

    @Override
    public Mono<PhotoDTO> partialUpdate(PhotoDTO photoDTO) {
        log.debug("Request to partially update Photo : {}", photoDTO);

        return photoRepository
            .findById(photoDTO.getId())
            .map(existingPhoto -> {
                photoMapper.partialUpdate(existingPhoto, photoDTO);

                return existingPhoto;
            })
            .flatMap(photoRepository::save)
            .map(photoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PhotoDTO> findAll() {
        log.debug("Request to get all Photos");
        return photoRepository.findAll().map(photoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return photoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PhotoDTO> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id).map(photoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        return photoRepository.deleteById(id);
    }
}
