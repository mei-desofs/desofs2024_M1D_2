import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReceiptMySuffix, NewReceiptMySuffix } from '../receipt-my-suffix.model';

export type PartialUpdateReceiptMySuffix = Partial<IReceiptMySuffix> & Pick<IReceiptMySuffix, 'id'>;

export type EntityResponseType = HttpResponse<IReceiptMySuffix>;
export type EntityArrayResponseType = HttpResponse<IReceiptMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ReceiptMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/receipts');

  create(receipt: NewReceiptMySuffix): Observable<EntityResponseType> {
    return this.http.post<IReceiptMySuffix>(this.resourceUrl, receipt, { observe: 'response' });
  }

  update(receipt: IReceiptMySuffix): Observable<EntityResponseType> {
    return this.http.put<IReceiptMySuffix>(`${this.resourceUrl}/${this.getReceiptMySuffixIdentifier(receipt)}`, receipt, {
      observe: 'response',
    });
  }

  partialUpdate(receipt: PartialUpdateReceiptMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IReceiptMySuffix>(`${this.resourceUrl}/${this.getReceiptMySuffixIdentifier(receipt)}`, receipt, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReceiptMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReceiptMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReceiptMySuffixIdentifier(receipt: Pick<IReceiptMySuffix, 'id'>): number {
    return receipt.id;
  }

  compareReceiptMySuffix(o1: Pick<IReceiptMySuffix, 'id'> | null, o2: Pick<IReceiptMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getReceiptMySuffixIdentifier(o1) === this.getReceiptMySuffixIdentifier(o2) : o1 === o2;
  }

  addReceiptMySuffixToCollectionIfMissing<Type extends Pick<IReceiptMySuffix, 'id'>>(
    receiptCollection: Type[],
    ...receiptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const receipts: Type[] = receiptsToCheck.filter(isPresent);
    if (receipts.length > 0) {
      const receiptCollectionIdentifiers = receiptCollection.map(receiptItem => this.getReceiptMySuffixIdentifier(receiptItem));
      const receiptsToAdd = receipts.filter(receiptItem => {
        const receiptIdentifier = this.getReceiptMySuffixIdentifier(receiptItem);
        if (receiptCollectionIdentifiers.includes(receiptIdentifier)) {
          return false;
        }
        receiptCollectionIdentifiers.push(receiptIdentifier);
        return true;
      });
      return [...receiptsToAdd, ...receiptCollection];
    }
    return receiptCollection;
  }
}
