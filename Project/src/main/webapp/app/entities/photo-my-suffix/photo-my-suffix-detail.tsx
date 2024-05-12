import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './photo-my-suffix.reducer';

export const PhotoMySuffixDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const photoEntity = useAppSelector(state => state.photo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="photoDetailsHeading">Photo</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{photoEntity.id}</dd>
          <dt>
            <span id="photo">Photo</span>
          </dt>
          <dd>
            {photoEntity.photo ? (
              <div>
                {photoEntity.photoContentType ? (
                  <a onClick={openFile(photoEntity.photoContentType, photoEntity.photo)}>
                    <img src={`data:${photoEntity.photoContentType};base64,${photoEntity.photo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {photoEntity.photoContentType}, {byteSize(photoEntity.photo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{photoEntity.date ? <TextFormat value={photoEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="state">State</span>
          </dt>
          <dd>{photoEntity.state}</dd>
          <dt>Portfolio</dt>
          <dd>{photoEntity.portfolio ? photoEntity.portfolio.id : ''}</dd>
          <dt>Cart</dt>
          <dd>{photoEntity.cart ? photoEntity.cart.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/photo-my-suffix" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/photo-my-suffix/${photoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhotoMySuffixDetail;
