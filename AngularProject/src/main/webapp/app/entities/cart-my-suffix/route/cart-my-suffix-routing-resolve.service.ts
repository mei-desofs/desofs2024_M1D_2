import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICartMySuffix } from '../cart-my-suffix.model';
import { CartMySuffixService } from '../service/cart-my-suffix.service';

const cartResolve = (route: ActivatedRouteSnapshot): Observable<null | ICartMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(CartMySuffixService)
      .find(id)
      .pipe(
        mergeMap((cart: HttpResponse<ICartMySuffix>) => {
          if (cart.body) {
            return of(cart.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default cartResolve;
