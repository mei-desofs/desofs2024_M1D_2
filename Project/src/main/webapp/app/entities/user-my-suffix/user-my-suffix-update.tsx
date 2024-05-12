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
import { IUserMySuffix } from 'app/shared/model/user-my-suffix.model';
import { getEntity, updateEntity, createEntity, reset } from './user-my-suffix.reducer';

export const UserMySuffixUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const portfolios = useAppSelector(state => state.portfolio.entities);
  const userEntity = useAppSelector(state => state.user.entity);
  const loading = useAppSelector(state => state.user.loading);
  const updating = useAppSelector(state => state.user.updating);
  const updateSuccess = useAppSelector(state => state.user.updateSuccess);

  const handleClose = () => {
    navigate('/user-my-suffix');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPortfolios({}));
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
      ...userEntity,
      ...values,
      portfolio: portfolios.find(it => it.id.toString() === values.portfolio?.toString()),
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
          ...userEntity,
          portfolio: userEntity?.portfolio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineShopApp.user.home.createOrEditLabel" data-cy="UserCreateUpdateHeading">
            Create or edit a User
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
                <ValidatedField name="id" required readOnly id="user-my-suffix-id" label="Id" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Email" id="user-my-suffix-email" name="email" data-cy="email" type="text" />
              <ValidatedField label="Password" id="user-my-suffix-password" name="password" data-cy="password" type="text" />
              <ValidatedField label="Address" id="user-my-suffix-address" name="address" data-cy="address" type="text" />
              <ValidatedField label="Contact" id="user-my-suffix-contact" name="contact" data-cy="contact" type="text" />
              <ValidatedBlobField
                label="Profile Photo"
                id="user-my-suffix-profilePhoto"
                name="profilePhoto"
                data-cy="profilePhoto"
                isImage
                accept="image/*"
              />
              <ValidatedField id="user-my-suffix-portfolio" name="portfolio" data-cy="portfolio" label="Portfolio" type="select">
                <option value="" key="0" />
                {portfolios
                  ? portfolios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-my-suffix" replace color="info">
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

export default UserMySuffixUpdate;
