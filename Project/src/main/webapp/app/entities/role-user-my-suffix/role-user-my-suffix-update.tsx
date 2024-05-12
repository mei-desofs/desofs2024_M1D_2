import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserMySuffix } from 'app/shared/model/user-my-suffix.model';
import { getEntities as getUsers } from 'app/entities/user-my-suffix/user-my-suffix.reducer';
import { IRoleMySuffix } from 'app/shared/model/role-my-suffix.model';
import { getEntities as getRoles } from 'app/entities/role-my-suffix/role-my-suffix.reducer';
import { IRoleUserMySuffix } from 'app/shared/model/role-user-my-suffix.model';
import { getEntity, updateEntity, createEntity, reset } from './role-user-my-suffix.reducer';

export const RoleUserMySuffixUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.user.entities);
  const roles = useAppSelector(state => state.role.entities);
  const roleUserEntity = useAppSelector(state => state.roleUser.entity);
  const loading = useAppSelector(state => state.roleUser.loading);
  const updating = useAppSelector(state => state.roleUser.updating);
  const updateSuccess = useAppSelector(state => state.roleUser.updateSuccess);

  const handleClose = () => {
    navigate('/role-user-my-suffix');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getRoles({}));
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
      ...roleUserEntity,
      ...values,
      userId: users.find(it => it.id.toString() === values.userId?.toString()),
      roleId: roles.find(it => it.id.toString() === values.roleId?.toString()),
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
          ...roleUserEntity,
          userId: roleUserEntity?.userId?.id,
          roleId: roleUserEntity?.roleId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineShopApp.roleUser.home.createOrEditLabel" data-cy="RoleUserCreateUpdateHeading">
            Create or edit a Role User
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
                <ValidatedField name="id" required readOnly id="role-user-my-suffix-id" label="Id" validate={{ required: true }} />
              ) : null}
              <ValidatedField id="role-user-my-suffix-userId" name="userId" data-cy="userId" label="User Id" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="role-user-my-suffix-roleId" name="roleId" data-cy="roleId" label="Role Id" type="select">
                <option value="" key="0" />
                {roles
                  ? roles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/role-user-my-suffix" replace color="info">
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

export default RoleUserMySuffixUpdate;
