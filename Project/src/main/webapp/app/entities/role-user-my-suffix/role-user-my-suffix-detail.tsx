import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './role-user-my-suffix.reducer';

export const RoleUserMySuffixDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roleUserEntity = useAppSelector(state => state.roleUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roleUserDetailsHeading">Role User</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{roleUserEntity.id}</dd>
          <dt>User Id</dt>
          <dd>{roleUserEntity.userId ? roleUserEntity.userId.id : ''}</dd>
          <dt>Role Id</dt>
          <dd>{roleUserEntity.roleId ? roleUserEntity.roleId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/role-user-my-suffix" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/role-user-my-suffix/${roleUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoleUserMySuffixDetail;
