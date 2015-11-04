
app.controller('RegistrationController', function($scope, $http) {
	$scope.submit = function() {
		if ($scope.text) {
			$http({
				method : 'POST',
				url : '/api/registration',
				data : $scope.text, // forms user object
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				if (data.errors) {
					// Showing errors.
					$scope.errorName = data.errors.name;
					$scope.errorUserName = data.errors.username;
					$scope.errorEmail = data.errors.email;
				} else {
					$scope.message = data.message;
				}
			});
		}
		;
	}
});