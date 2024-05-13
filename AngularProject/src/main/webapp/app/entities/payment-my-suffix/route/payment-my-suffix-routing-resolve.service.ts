import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { PaymentMySuffixService } from '../service/payment-my-suffix.service';

const paymentResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaymentMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaymentMySuffixService)
      .find(id)
      .pipe(
        mergeMap((payment: HttpResponse<IPaymentMySuffix>) => {
          if (payment.body) {
            return of(payment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paymentResolve;
