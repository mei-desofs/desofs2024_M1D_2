import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentMySuffix, NewPaymentMySuffix } from '../payment-my-suffix.model';

export type PartialUpdatePaymentMySuffix = Partial<IPaymentMySuffix> & Pick<IPaymentMySuffix, 'id'>;

type RestOf<T extends IPaymentMySuffix | NewPaymentMySuffix> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPaymentMySuffix = RestOf<IPaymentMySuffix>;

export type NewRestPaymentMySuffix = RestOf<NewPaymentMySuffix>;

export type PartialUpdateRestPaymentMySuffix = RestOf<PartialUpdatePaymentMySuffix>;

export type EntityResponseType = HttpResponse<IPaymentMySuffix>;
export type EntityArrayResponseType = HttpResponse<IPaymentMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class PaymentMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments');

  create(payment: NewPaymentMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .post<RestPaymentMySuffix>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(payment: IPaymentMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .put<RestPaymentMySuffix>(`${this.resourceUrl}/${this.getPaymentMySuffixIdentifier(payment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(payment: PartialUpdatePaymentMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .patch<RestPaymentMySuffix>(`${this.resourceUrl}/${this.getPaymentMySuffixIdentifier(payment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPaymentMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaymentMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaymentMySuffixIdentifier(payment: Pick<IPaymentMySuffix, 'id'>): number {
    return payment.id;
  }

  comparePaymentMySuffix(o1: Pick<IPaymentMySuffix, 'id'> | null, o2: Pick<IPaymentMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentMySuffixIdentifier(o1) === this.getPaymentMySuffixIdentifier(o2) : o1 === o2;
  }

  addPaymentMySuffixToCollectionIfMissing<Type extends Pick<IPaymentMySuffix, 'id'>>(
    paymentCollection: Type[],
    ...paymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const payments: Type[] = paymentsToCheck.filter(isPresent);
    if (payments.length > 0) {
      const paymentCollectionIdentifiers = paymentCollection.map(paymentItem => this.getPaymentMySuffixIdentifier(paymentItem));
      const paymentsToAdd = payments.filter(paymentItem => {
        const paymentIdentifier = this.getPaymentMySuffixIdentifier(paymentItem);
        if (paymentCollectionIdentifiers.includes(paymentIdentifier)) {
          return false;
        }
        paymentCollectionIdentifiers.push(paymentIdentifier);
        return true;
      });
      return [...paymentsToAdd, ...paymentCollection];
    }
    return paymentCollection;
  }

  protected convertDateFromClient<T extends IPaymentMySuffix | NewPaymentMySuffix | PartialUpdatePaymentMySuffix>(payment: T): RestOf<T> {
    return {
      ...payment,
      date: payment.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPaymentMySuffix: RestPaymentMySuffix): IPaymentMySuffix {
    return {
      ...restPaymentMySuffix,
      date: restPaymentMySuffix.date ? dayjs(restPaymentMySuffix.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPaymentMySuffix>): HttpResponse<IPaymentMySuffix> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPaymentMySuffix[]>): HttpResponse<IPaymentMySuffix[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
