import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICartMySuffix, NewCartMySuffix } from '../cart-my-suffix.model';

export type PartialUpdateCartMySuffix = Partial<ICartMySuffix> & Pick<ICartMySuffix, 'id'>;

export type EntityResponseType = HttpResponse<ICartMySuffix>;
export type EntityArrayResponseType = HttpResponse<ICartMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class CartMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carts');

  create(cart: NewCartMySuffix): Observable<EntityResponseType> {
    return this.http.post<ICartMySuffix>(this.resourceUrl, cart, { observe: 'response' });
  }

  update(cart: ICartMySuffix): Observable<EntityResponseType> {
    return this.http.put<ICartMySuffix>(`${this.resourceUrl}/${this.getCartMySuffixIdentifier(cart)}`, cart, { observe: 'response' });
  }

  partialUpdate(cart: PartialUpdateCartMySuffix): Observable<EntityResponseType> {
    return this.http.patch<ICartMySuffix>(`${this.resourceUrl}/${this.getCartMySuffixIdentifier(cart)}`, cart, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICartMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICartMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCartMySuffixIdentifier(cart: Pick<ICartMySuffix, 'id'>): number {
    return cart.id;
  }

  compareCartMySuffix(o1: Pick<ICartMySuffix, 'id'> | null, o2: Pick<ICartMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getCartMySuffixIdentifier(o1) === this.getCartMySuffixIdentifier(o2) : o1 === o2;
  }

  addCartMySuffixToCollectionIfMissing<Type extends Pick<ICartMySuffix, 'id'>>(
    cartCollection: Type[],
    ...cartsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const carts: Type[] = cartsToCheck.filter(isPresent);
    if (carts.length > 0) {
      const cartCollectionIdentifiers = cartCollection.map(cartItem => this.getCartMySuffixIdentifier(cartItem));
      const cartsToAdd = carts.filter(cartItem => {
        const cartIdentifier = this.getCartMySuffixIdentifier(cartItem);
        if (cartCollectionIdentifiers.includes(cartIdentifier)) {
          return false;
        }
        cartCollectionIdentifiers.push(cartIdentifier);
        return true;
      });
      return [...cartsToAdd, ...cartCollection];
    }
    return cartCollection;
  }
}
