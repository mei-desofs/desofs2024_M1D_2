import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './receipt-my-suffix.reducer';

export const ReceiptMySuffixDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const receiptEntity = useAppSelector(state => state.receipt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="receiptDetailsHeading">Receipt</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{receiptEntity.id}</dd>
          <dt>
            <span id="idPayment">Id Payment</span>
          </dt>
          <dd>{receiptEntity.idPayment}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{receiptEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/receipt-my-suffix" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/receipt-my-suffix/${receiptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReceiptMySuffixDetail;
