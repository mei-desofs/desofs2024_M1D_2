import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-my-suffix.reducer';

export const UserMySuffixDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userEntity = useAppSelector(state => state.user.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userDetailsHeading">User</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{userEntity.id}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{userEntity.email}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{userEntity.password}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{userEntity.address}</dd>
          <dt>
            <span id="contact">Contact</span>
          </dt>
          <dd>{userEntity.contact}</dd>
          <dt>
            <span id="profilePhoto">Profile Photo</span>
          </dt>
          <dd>
            {userEntity.profilePhoto ? (
              <div>
                {userEntity.profilePhotoContentType ? (
                  <a onClick={openFile(userEntity.profilePhotoContentType, userEntity.profilePhoto)}>
                    <img
                      src={`data:${userEntity.profilePhotoContentType};base64,${userEntity.profilePhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {userEntity.profilePhotoContentType}, {byteSize(userEntity.profilePhoto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Portfolio</dt>
          <dd>{userEntity.portfolio ? userEntity.portfolio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-my-suffix" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-my-suffix/${userEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserMySuffixDetail;
