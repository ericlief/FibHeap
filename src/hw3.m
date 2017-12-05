 
wIHB = [ 1.4 0.4 0.0
        -2.0 0.8 -0.6]  

wHOB = [2.1 -1.0 0.4
        1.0 1.1 -0.3] 

p = [-1; 1];
d = [.3; .3];
a = 1.5
lambda = 2.0; % slope of transfer function

%bH = [ 0.00000
%       0.50000];

%bO = -0.50000;
% Hence the weights from the input layer into the hidden layer with added virtual
% neuron with fixed output 1 (for representing thresholds) are:

%w_i_hb = [w_i_h b_h]
 
zi = wIHB * [p;1]
yi = logsig(wIHB * [p;1])
%yO = logsig(logsig(w[p;1])

zj = wHOB * [yi;1]
yO = logsig(wHOB * [yi;1])

eO = d - yO
df = yO .* (1 - yO)
deltaO = eO .* lambda .* yO .* (1 - yO)
%newWHOB = wHOB + a*deltaO * yi'
a*deltaO' * yi


