import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPortfolioMySuffix } from 'app/shared/model/portfolio-my-suffix.model';
import { getEntities as getPortfolios } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.reducer';
import { ICartMySuffix } from 'app/shared/model/cart-my-suffix.model';
import { getEntities as getCarts } from 'app/entities/cart-my-suffix/cart-my-suffix.reducer';
import { IPhotoMySuffix } from 'app/shared/model/photo-my-suffix.model';
import { PhotoState } from 'app/shared/model/enumerations/photo-state.model';
import { getEntity, updateEntity, createEntity, reset } from './photo-my-suffix.reducer';

export const PhotoMySuffixUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const portfolios = useAppSelector(state => state.portfolio.entities);
  const carts = useAppSelector(state => state.cart.entities);
  const photoEntity = useAppSelector(state => state.photo.entity);
  const loading = useAppSelector(state => state.photo.loading);
  const updating = useAppSelector(state => state.photo.updating);
  const updateSuccess = useAppSelector(state => state.photo.updateSuccess);
  const photoStateValues = Object.keys(PhotoState);

  const handleClose = () => {
    navigate('/photo-my-suffix');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPortfolios({}));
    dispatch(getCarts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...photoEntity,
      ...values,
      portfolio: portfolios.find(it => it.id.toString() === values.portfolio?.toString()),
      cart: carts.find(it => it.id.toString() === values.cart?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          state: 'ACTIVE',
          ...photoEntity,
          portfolio: photoEntity?.portfolio?.id,
          cart: photoEntity?.cart?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineShopApp.photo.home.createOrEditLabel" data-cy="PhotoCreateUpdateHeading">
            Create or edit a Photo
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="photo-my-suffix-id" label="Id" validate={{ required: true }} />
              ) : null}
              <ValidatedBlobField label="Photo" id="photo-my-suffix-photo" name="photo" data-cy="photo" isImage accept="image/*" />
              <ValidatedField label="Date" id="photo-my-suffix-date" name="date" data-cy="date" type="date" />
              <ValidatedField label="State" id="photo-my-suffix-state" name="state" data-cy="state" type="select">
                {photoStateValues.map(photoState => (
                  <option value={photoState} key={photoState}>
                    {photoState}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="photo-my-suffix-portfolio" name="portfolio" data-cy="portfolio" label="Portfolio" type="select">
                <option value="" key="0" />
                {portfolios
                  ? portfolios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="photo-my-suffix-cart" name="cart" data-cy="cart" label="Cart" type="select">
                <option value="" key="0" />
                {carts
                  ? carts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/photo-my-suffix" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PhotoMySuffixUpdate;
